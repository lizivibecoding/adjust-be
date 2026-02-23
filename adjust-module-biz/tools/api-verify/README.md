## 同分接口结果验证 + 性能对比（最小版）

覆盖两个查询接口：

- `GET /app-api/biz/adjustment/same-score`
- `GET /app-api/biz/adjustment/same-score-stat`

### 前置

- 本地服务可访问：`http://127.0.0.1:48080/app-api`
- 使用 Bearer Token 鉴权（脚本里会自动加 `Authorization: Bearer <TOKEN>`）

先打开 `same-score.verify.mjs`，填写：

- `TOKEN`：你的 token（不含 `Bearer ` 前缀）
- `BASE_URL`：例如 `http://127.0.0.1:48080/app-api`

### 1) 记录基线（优化前）

在 `adjust-be/adjust-module-biz/tools/api-verify/` 目录执行：

```bash
node same-score.verify.mjs record --tag before --warmup 5 --iters 30
```

会输出每个接口的 p50/p95/avg 等耗时，并保存基线到：

`artifacts/same-score-verify/before/`

### 2) 验证一致性 + 对比提升（优化后）

```bash
node same-score.verify.mjs verify --tag after --against before --warmup 5 --iters 30
```

- **一致性**：比较优化前后的响应快照（归一化后做 sha256），不一致会提示 `DIFF`
- **提升**：输出 avg / p95 的提升百分比（正数表示更快）

详细产物：

- `artifacts/same-score-verify/<tag>/*.normalized.json`：归一化后的响应（便于 diff）
- `artifacts/same-score-verify/<tag>/*.bench.json`：每次请求耗时列表
- `artifacts/same-score-verify/<tag>/report.json`：一致性 + 提升汇总

### 当前固定用例（按你截图）

脚本内置单用例，且 **不传** `schoolLevel/provinceCode/studyMode`：

- same-score：`year=2025&beginScore=300&endScore=500&pageNo=1&pageSize=10`
- same-score-stat：`year=2025&beginScore=300&endScore=500`

