# 电商框架

## 更新日志
### 2019-7-10：
	创建项目，采用模块化编程，将项目分为应用、注解、编译、核心、功能五个模块
	对项目进行封装，将fragment与activity的基础方法和必须方法抽出作为两个父类
	所有开发需要的fragment和activity全部继承这两个父类，简化开发流程
	并将ButterKnife加入父类，子类无需使用findViewById()等寻找view的代码

### 2019-7-12:
	采用okhttp3与retrofit2构建网络请求框架，使用建造者模式编写网络请求工具，方便网络请求

### 2019-7-13：
	项目完全改用AndroidX开发
	加入对Java8的支持，完全采用lambda表达式编写，缩减代码量，提高编写效率

### 2019-7-14：
	完成闪屏页、引导页的开发
	编写登录注册界面，完成登录注册逻辑

### 2019-7-15：
	完善项目结构，开始编写主界面布局
	主界面分为学习、兴趣、市场、聊天四个版块
	首先实现兴趣版块

### 2019-7-16：
	对兴趣版块进行划分，分为关注、发现、话题
	已完成对发现区的界面编写和相关业务逻辑的编写
	加入网络请求相关逻辑

### 2019-7-17：
	将ObjectBox作为本地数据库，创建数据库操作类，工具类，及JavaBean文件
	改善网络请求逻辑，极大减少了刷新数据所需要请求服务器的次数，缓解服务器压力

### 2019-7-21:
	完成话题页和关注页的界面编写和数据加载

### 2019-7-24:
	完成话题二级界面的显示和数据加载

### 2019-7-25：
	完成文章和动态的详情页
	以底部弹出Dialog作为评论窗口

### 2019-7-26：
	详情页增加查看转发、点赞、评论用户列表的功能

### 2019-7-27：
	引入Immersionbar状态栏管理框架，适配华为、小米全面屏手机以及刘海屏手机
	利用框架实现状态栏透明、渐变等效果，提升界面美观度

### 2019-7-28：
	增加发表帖子的页面