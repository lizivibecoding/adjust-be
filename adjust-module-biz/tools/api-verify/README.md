## 同分接口结果验证 + 性能对比（最小版）

覆盖两个查询接口：

- `GET /app-api/biz/adjustment/same-score`
- `GET /app-api/biz/adjustment/same-score-stat`

### 前置

- 本地服务可访问：`http://127.0.0.1:48080/app-api`
- 使用 Bearer Token 鉴权（脚本里会自动加 `Authorization: Bearer <TOKEN>`）

先打开 `same-score.verify.js`，填写：

- `TOKEN`：你的 token（不含 `Bearer ` 前缀）
- `BASE_URL`：例如 `http://127.0.0.1:48080/app-api`

### 1) 记录基线（优化前）

在 `adjust-be/adjust-module-biz/tools/api-verify/` 目录执行：

```bash
node same-score.verify.js record --warmup 5 --iters 30
```

会输出每个接口的 p50/p95/avg 等耗时，并保存基线到：

`artifacts/same-score-verify/before/`（固定目录）

说明：重复执行会先删除 `before/after` 目录再重新生成（相当于覆盖）。

### 2) 验证一致性 + 对比提升（优化后）

```bash
node same-score.verify.js verify --warmup 5 --iters 30
```

- **一致性**：比较优化前后的响应快照（归一化后做 sha256），不一致会提示 `DIFF`
- **提升**：输出 avg / p95 的提升百分比（正数表示更快）

详细产物：

- `artifacts/same-score-verify/before/*.normalized.json`：基线归一化响应（便于 diff）
- `artifacts/same-score-verify/before/*.bench.json`：基线耗时列表
- `artifacts/same-score-verify/after/*.normalized.json`：优化后归一化响应
- `artifacts/same-score-verify/after/*.bench.json`：优化后耗时列表
- `artifacts/same-score-verify/after/report.json`：一致性 + 提升汇总

### 当前固定用例（按你截图）

脚本内置单用例，且 **不传** `schoolLevel/provinceCode/studyMode`：

- same-score：`year=2025&beginScore=300&endScore=500&pageNo=1&pageSize=10`
- same-score-stat：`year=2025&beginScore=300&endScore=500`

---

## Search 接口结果验证 + 性能对比（两页）

覆盖一个查询接口（跑两页）：

- `GET /app-api/biz/adjustment/search`

### 前置

- 本地服务可访问：`http://127.0.0.1:48080/app-api`
- 使用 Bearer Token 鉴权

先打开 `search.verify.js`，填写：

- `TOKEN`：你的 token（不含 `Bearer ` 前缀）
- `BASE_URL`：例如 `http://127.0.0.1:48080/app-api`
- （可选）`TENANT_ID`：如果你本地需要 `tenant-id` header

### 1) 记录基线（优化前）

在 `adjust-be/adjust-module-biz/tools/api-verify/` 目录执行：

```bash
node search.verify.js record --warmup 5 --iters 30
```

产物保存到：

`artifacts/search-verify/before/`（固定目录）

说明：重复执行会先删除 `before/after` 目录再重新生成（相当于覆盖）。

### 2) 验证一致性 + 对比提升（优化后）

```bash
node search.verify.js verify --warmup 5 --iters 30
```

- **一致性**：比较优化前后的响应快照（归一化后做 sha256），不一致会提示 `DIFF`
- **提升**：输出 avg / p95 的提升百分比（正数表示更快）

详细产物：

- `artifacts/search-verify/before/*.normalized.json`
- `artifacts/search-verify/before/*.bench.json`
- `artifacts/search-verify/after/*.normalized.json`
- `artifacts/search-verify/after/*.bench.json`
- `artifacts/search-verify/after/report.json`

### 当前固定用例（按你给的 curl）

脚本内置单用例，跑两页：

- `tabType=major&majorCode=08&pageNo=1&pageSize=10`
- `tabType=major&majorCode=08&pageNo=2&pageSize=10`

