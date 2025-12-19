# Compose原理简易实现

## 一、SlotTable

### 功能：
*   核心“记忆仓库”，线性存储所有 Composable 的状态（remember 值、参数值、lambda 等）
* 	管理 index 指针，支持顺序访问
* 	管理 Group：startGroup / endGroup / skipGroup
* 	startGroup：记录 group 起始 index
* 	endGroup：计算 group 占用 slot 数量
* 	skipGroup：重组时快速跳过整个 group

### 原理：
*	每次调用 next() → 返回当前 slot 并 index++
*	每次调用 update() → 修改 index-1 对应的 slot

---

## 二、Composer

### 功能：
*	SlotTable 封装器：提供更高层的接口
*	changed(value) → 判断当前 slot 是否和传入值不同，返回 Boolean
*	remember(factory) → 读取 slot，如果为空执行 lambda 并写入 slot
*	Group 管理：startGroup() / endGroup() / skipGroup()

### 原理：
*	将 Composable 函数的运行与 SlotTable 状态绑定
*	控制 skip / 重组逻辑

---

## 三、State

### 功能：
*	可变状态持有者
*	value 变化时触发回调 → 触发重组

### 原理：
*	内部存储当前值 _value
*	set(value) → 值改变 → 调用 onChange()
*	mutableStateOf 是创建 State 的工厂函数

---

## 四、Composition

### 功能：
*	管理整个 Composable 树的入口
*	setContent() → 设置顶层 Composable
*	recompose() → 重组整个树
*	内部持有 SlotTable + Composer

### 原理：
*	每次 recompose() → reset SlotTable → 执行顶层 Composable → 根据 changed/skip 更新 slot
*	可由 State 改变触发

---

## 五、Composable 函数

### 功能：
*	业务逻辑 + UI 表达
*	使用 composer.changed() 判断参数是否变化

---

## Android View系统 对比 Compose
| 特性            | View 系统                           | Compose                         |
|---------------|-----------------------------------|---------------------------------|
| UI架构          | View对象组成的UI Tree                  | Composable 函数描述UI Tree          |
| 渲染对象          | 每个 View 占用对象内存                    | SlotTable 记录状态、重组时决定是否绘制        |
| Draw / Canvas | 每个 View 负责绘制自己                    | Applier（类似 Canvas）统一提交 UI 树变更   |
| 重绘策略          | requestLayout/invalidate() → 递归绘制 | State 改变 → Recomposer → 重组 → 重绘 |