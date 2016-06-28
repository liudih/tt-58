TomTop product Project
======================

This is a spring boot Application.


Development Environment Setup
------------------------------

我们采用Docker容器提供应用外围的服务，包括PostgreSQL，Redis等。
不懂的请参考tomtopwebsite项目的readme


启动项目的方式
------------------------------
项目有两种方式启动
1.通过类Application.java main方法直接运行
2.在项目根目录下面运行cmd,然后运行 mvn spring-boot:run

------------------------------
Page pageObj = Page.getPage(currentPage, count, pageSize);
currentPage : 当前页
count:记录总数
page:一页显示多少条数量


所有controllers接口 返回 均为Result 对象
ret ：1: 成功 or 0: 失败
data: 需要返回的数据集
page: 分页对象

