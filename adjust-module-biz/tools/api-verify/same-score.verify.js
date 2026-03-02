const { performance } = require("node:perf_hooks")
const fs = require("node:fs")
const path = require("node:path")
const crypto = require("node:crypto")

// ====== 配置（按需改这里）======
// token 不含 "Bearer " 前缀
const TOKEN = "CHANGE_ME"
// 注意：包含 /app-api 前缀（与 curl 一致）
const BASE_URL = "http://127.0.0.1:48080/app-api"

function usageAndExit(code = 1) {
  // Keep it short; user runs locally.
  console.log(
    [
      "Usage:",
      "  node same-score.verify.js record",
      "  node same-score.verify.js verify",
      "",
      "Config:",
      "  Edit TOKEN / BASE_URL constants in this script",
      "",
      "Options:",
      "  --iters <n>            benchmark iterations per endpoint (default 30)",
      "  --warmup <n>           warmup iterations per endpoint (default 5)"
    ].join("\n")
  )
  process.exit(code)
}

function parseArgs(argv) {
  const args = { _: [] }
  for (let i = 0; i < argv.length; i++) {
    const a = argv[i]
    if (a === "--iters") args.iters = Number(argv[++i])
    else if (a === "--warmup") args.warmup = Number(argv[++i])
    else if (a.startsWith("--")) {
      console.error(`Unknown option: ${a}`)
      usageAndExit(2)
    } else args._.push(a)
  }
  return args
}

function ensureDir(p) {
  fs.mkdirSync(p, { recursive: true })
}

function resetDir(p) {
  // Re-run friendly: delete old artifacts and recreate.
  try {
    fs.rmSync(p, { recursive: true, force: true })
  } catch (_) {
    // ignore
  }
  ensureDir(p)
}

function sha256(s) {
  return crypto.createHash("sha256").update(s).digest("hex")
}

function stableStringify(value) {
  // Canonical JSON: sort object keys recursively to make diffs stable.
  const seen = new WeakSet()
  const normalize = (v) => {
    if (v === null || typeof v !== "object") return v
    if (seen.has(v)) return "[Circular]"
    seen.add(v)
    if (Array.isArray(v)) return v.map(normalize)
    const keys = Object.keys(v).sort()
    const out = {}
    for (const k of keys) out[k] = normalize(v[k])
    return out
  }
  return JSON.stringify(normalize(value), null, 2)
}

function stats(msList) {
  const xs = msList
    .filter((v) => Number.isFinite(v))
    .slice()
    .sort((a, b) => a - b)
  if (xs.length === 0) return null
  const sum = xs.reduce((a, b) => a + b, 0)
  const pick = (p) => xs[Math.min(xs.length - 1, Math.max(0, Math.ceil(p * xs.length) - 1))]
  return {
    n: xs.length,
    minMs: xs[0],
    p50Ms: pick(0.5),
    p90Ms: pick(0.9),
    p95Ms: pick(0.95),
    maxMs: xs[xs.length - 1],
    avgMs: sum / xs.length
  }
}

function formatStats(label, s) {
  if (!s) return `${label}: (no data)`
  const f = (x) => (x == null ? "?" : x.toFixed(2))
  return [
    `${label}: n=${s.n}`,
    `min=${f(s.minMs)}ms`,
    `p50=${f(s.p50Ms)}ms`,
    `p90=${f(s.p90Ms)}ms`,
    `p95=${f(s.p95Ms)}ms`,
    `max=${f(s.maxMs)}ms`,
    `avg=${f(s.avgMs)}ms`
  ].join(" ")
}

function normalizeSameScore(resp) {
  // Keep semantics; only make ordering stable to avoid false diffs.
  const r = structuredClone(resp)
  const list = r?.data?.list
  if (Array.isArray(list)) {
    list.sort((a, b) => {
      const ka = [
        a?.schoolId ?? "",
        a?.collegeId ?? "",
        a?.majorId ?? "",
        a?.year ?? "",
        a?.studyMode ?? "",
        a?.majorCode ?? ""
      ].map(String)
      const kb = [
        b?.schoolId ?? "",
        b?.collegeId ?? "",
        b?.majorId ?? "",
        b?.year ?? "",
        b?.studyMode ?? "",
        b?.majorCode ?? ""
      ].map(String)
      for (let i = 0; i < ka.length; i++) {
        const c = ka[i].localeCompare(kb[i])
        if (c !== 0) return c
      }
      // Tiebreaker by score (desc) to keep stable ordering if duplicates exist.
      const sa = Number(a?.firstScore ?? -Infinity)
      const sb = Number(b?.firstScore ?? -Infinity)
      if (Number.isFinite(sa) && Number.isFinite(sb) && sa !== sb) return sb - sa
      return 0
    })
  }
  return r
}

function normalizeSameScoreStat(resp) {
  const r = structuredClone(resp)
  const list = r?.data
  if (Array.isArray(list)) {
    list.sort((a, b) => {
      const ka = [a?.name ?? "", a?.subName ?? ""].map(String)
      const kb = [b?.name ?? "", b?.subName ?? ""].map(String)
      const c1 = ka[0].localeCompare(kb[0])
      if (c1 !== 0) return c1
      return ka[1].localeCompare(kb[1])
    })
  }
  return r
}

async function httpJson(url, token, debugDir) {
  const t0 = performance.now()
  const res = await fetch(url, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
      Accept: "*/*",
      "User-Agent": "curl/8.0.0"
    }
  })
  const text = await res.text()
  const t1 = performance.now()
  let json
  try {
    json = text ? JSON.parse(text) : null
  } catch (e) {
    const snippet = text?.slice(0, 300)
    try {
      if (debugDir) {
        ensureDir(debugDir)
        const ext = (res.headers.get("content-type") || "").includes("html") ? "html" : "txt"
        const file = path.join(debugDir, `non-json-${Date.now()}.${ext}`)
        fs.writeFileSync(file, text ?? "")
      }
    } catch (_) {
      // ignore
    }
    const contentType = res.headers.get("content-type") || ""
    throw new Error(
      `Non-JSON response: HTTP ${res.status}. content-type=${contentType}. final-url=${res.url}. Body starts: ${snippet}`
    )
  }
  if (!res.ok) {
    throw new Error(`HTTP ${res.status} for ${url}. Body: ${stableStringify(json).slice(0, 800)}`)
  }
  return { ms: t1 - t0, json }
}

async function benchEndpoint({ name, url, token, warmup, iters, normalize, debugDir }) {
  const warm = []
  for (let i = 0; i < warmup; i++) {
    const { ms } = await httpJson(url, token, debugDir)
    warm.push(ms)
  }
  const times = []
  let lastJson = null
  for (let i = 0; i < iters; i++) {
    const { ms, json } = await httpJson(url, token, debugDir)
    times.push(ms)
    lastJson = json
  }
  const s = stats(times)
  const normalized = normalize(lastJson)
  return { name, url, warmupMs: warm, timesMs: times, stats: s, normalized }
}

function loadRun(runDir) {
  const metaPath = path.join(runDir, "meta.json")
  const meta = JSON.parse(fs.readFileSync(metaPath, "utf8"))
  const sameScore = JSON.parse(fs.readFileSync(path.join(runDir, "same-score.normalized.json"), "utf8"))
  const sameScoreStat = JSON.parse(
    fs.readFileSync(path.join(runDir, "same-score-stat.normalized.json"), "utf8")
  )
  return { meta, sameScore, sameScoreStat }
}

function diffHashes(aPath, bPath) {
  const a = fs.readFileSync(aPath, "utf8").trim()
  const b = fs.readFileSync(bPath, "utf8").trim()
  return a === b
}

function improvement(before, after) {
  // positive means faster (lower ms)
  if (!before?.avgMs || !after?.avgMs) return null
  return ((before.avgMs - after.avgMs) / before.avgMs) * 100
}

async function main() {
  const args = parseArgs(process.argv.slice(2))
  const mode = args._[0]
  if (!mode) usageAndExit(2)
  if (mode !== "record" && mode !== "verify") {
    console.error(`Unknown mode: ${mode}`)
    usageAndExit(2)
  }

  const token = TOKEN
  if (!token || token === "CHANGE_ME") {
    console.error('Missing TOKEN constant in script (set TOKEN, bearer token without "Bearer " prefix).')
    usageAndExit(2)
  }

  const baseUrl = (BASE_URL ?? "http://127.0.0.1:48080/app-api").replace(/\/+$/, "")
  const warmup = Number.isFinite(args.warmup) ? args.warmup : 5
  const iters = Number.isFinite(args.iters) ? args.iters : 30

  // User confirmed: do NOT pass schoolLevel/provinceCode/studyMode.
  // Use single test case only.
  const qsSameScore = new URLSearchParams({
    year: "2025",
    beginScore: "300",
    endScore: "500",
    pageNo: "1",
    pageSize: "10"
  }).toString()
  const qsStat = new URLSearchParams({
    year: "2025",
    beginScore: "300",
    endScore: "500"
  }).toString()

  const endpoints = [
    {
      name: "same-score",
      url: `${baseUrl}/biz/adjustment/same-score?${qsSameScore}`,
      normalize: normalizeSameScore
    },
    {
      name: "same-score-stat",
      url: `${baseUrl}/biz/adjustment/same-score-stat?${qsStat}`,
      normalize: normalizeSameScoreStat
    }
  ]

  const root = path.resolve(process.cwd(), "artifacts", "same-score-verify")
  ensureDir(root)

  const tag = mode === "record" ? "before" : "after"
  const runDir = path.join(root, tag)
  const baseDir = path.join(root, "before")

  if (mode === "verify" && !fs.existsSync(baseDir)) {
    console.error(`Baseline not found: ${baseDir}`)
    process.exit(2)
  }
  resetDir(runDir)

  const results = []
  for (const ep of endpoints) {
    console.log(`${mode === "record" ? "Recording" : "Verifying"} ${ep.name} ...`)
    results.push(await benchEndpoint({ ...ep, token, warmup, iters, debugDir: path.join(runDir, "debug") }))
  }

  const meta = {
    tag,
    mode,
    baseUrl,
    warmup,
    iters,
    createdAt: new Date().toISOString(),
    endpoints: results.map((r) => ({ name: r.name, url: r.url, stats: r.stats }))
  }
  fs.writeFileSync(path.join(runDir, "meta.json"), stableStringify(meta))

  for (const r of results) {
    const normJsonPath = path.join(runDir, `${r.name}.normalized.json`)
    const normText = stableStringify(r.normalized)
    fs.writeFileSync(normJsonPath, normText)
    fs.writeFileSync(path.join(runDir, `${r.name}.sha256`), sha256(normText) + "\n")
    fs.writeFileSync(path.join(runDir, `${r.name}.bench.json`), stableStringify({ stats: r.stats, timesMs: r.timesMs }))
    console.log(formatStats(r.name, r.stats))
  }

  if (mode === "record") {
    console.log(`Saved baseline to: ${runDir}`)
    return
  }

  const okSameScore = diffHashes(path.join(baseDir, "same-score.sha256"), path.join(runDir, "same-score.sha256"))
  const okStat = diffHashes(
    path.join(baseDir, "same-score-stat.sha256"),
    path.join(runDir, "same-score-stat.sha256")
  )

  const baseRun = loadRun(baseDir)
  const curRun = loadRun(runDir)
  const baseStats = Object.fromEntries(baseRun.meta.endpoints.map((e) => [e.name, e.stats]))
  const curStats = Object.fromEntries(curRun.meta.endpoints.map((e) => [e.name, e.stats]))

  const report = {
    accuracy: {
      sameScoreHashEqual: okSameScore,
      sameScoreStatHashEqual: okStat
    },
    performance: {
      sameScore: {
        before: baseStats["same-score"],
        after: curStats["same-score"],
        avgImprovementPct: improvement(baseStats["same-score"], curStats["same-score"]),
        p95ImprovementPct:
          baseStats["same-score"]?.p95Ms && curStats["same-score"]?.p95Ms
            ? ((baseStats["same-score"].p95Ms - curStats["same-score"].p95Ms) / baseStats["same-score"].p95Ms) *
              100
            : null
      },
      sameScoreStat: {
        before: baseStats["same-score-stat"],
        after: curStats["same-score-stat"],
        avgImprovementPct: improvement(baseStats["same-score-stat"], curStats["same-score-stat"]),
        p95ImprovementPct:
          baseStats["same-score-stat"]?.p95Ms && curStats["same-score-stat"]?.p95Ms
            ? ((baseStats["same-score-stat"].p95Ms - curStats["same-score-stat"].p95Ms) /
                baseStats["same-score-stat"].p95Ms) *
              100
            : null
      }
    }
  }
  fs.writeFileSync(path.join(runDir, "report.json"), stableStringify(report))

  console.log("")
  console.log("Accuracy:")
  console.log(`  same-score:      ${okSameScore ? "OK" : "DIFF (see normalized json)"}`)
  console.log(`  same-score-stat: ${okStat ? "OK" : "DIFF (see normalized json)"}`)
  console.log("")
  console.log("Performance (avg / p95 improvement; positive=faster):")
  const s1 = report.performance.sameScore
  const s2 = report.performance.sameScoreStat
  console.log(
    `  same-score:      avg ${s1.avgImprovementPct == null ? "?" : s1.avgImprovementPct.toFixed(2) + "%"}, p95 ${
      s1.p95ImprovementPct == null ? "?" : s1.p95ImprovementPct.toFixed(2) + "%"
    }`
  )
  console.log(
    `  same-score-stat: avg ${s2.avgImprovementPct == null ? "?" : s2.avgImprovementPct.toFixed(2) + "%"}, p95 ${
      s2.p95ImprovementPct == null ? "?" : s2.p95ImprovementPct.toFixed(2) + "%"
    }`
  )

  if (!okSameScore || !okStat) {
    process.exitCode = 1
  }
  console.log(`\nSaved run to: ${runDir}`)
}

main().catch((e) => {
  console.error(e)
  process.exit(1)
})

