<#setting classic_compatible=true>
<#assign base=request.contextPath />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>用户列表</title>
    <link href="${base}/css/viewer.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/css/bootstrap.min.css" rel="stylesheet" type="text/css" media="screen">

</head>
<body>

<!-- START CONTENT -->

<section class="wrapper main-wrapper row" style=''>

    <div class="col-lg-12">
        <section class="box ">
            <header class="panel_header">
                <h2 class="title pull-left">用户列表</h2>
                <div class="actions panel_actions pull-right">
                    <a class="box_toggle fa fa-chevron-down"></a>
                    <a class="box_setting fa fa-cog" data-toggle="modal" href="#section-settings"></a>
                    <a class="box_close fa fa-times"></a>
                </div>
            </header>
            <div class="content-body">
                <div class="row">
                    <div class="col-xs-12">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>Id</th>
                                <th>登录名</th>
                                <th>姓名</th>
                                <th>生日</th>
                                <th>邮箱</th>
                                <th>性别</th>
                            </tr>
                            </thead>
                            <tbody>
                                <#if page.list??>
                                    <#list page.list as item>
                                    <tr scope="row">
                                        <td>${item.id}</td>
                                        <td>${item.username}</td>
                                        <td>${item.nickname}</td>
                                        <td>${(item.birthday?string('yyyy-mm-dd'))!'无数据'}</td>
                                        <td>${item.email}</td>
                                        <td>${(item.gender == 1)?string('男','女')}</td>
                                    </tr>
                                    </#list>
                                <#else>
                                    <p>   抱歉!暂时无数据 </p>
                                </#if>
                            </tr>
                            </tbody>
                        </table>

                    </div>
                </div>

                <div class="row">
                    <div class="col-xs-6">
                        <div class="message">
                            共<i class="blue">${page.total}</i>条记录，总页数：${page.pages}  当前显示第 <i
                                class="blue">${page.pageNum}/${page.pages}</i> 页
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="dataTables_paginate paging_bootstrap">
                            <ul class="pagination">
                                <#if !page.isFirstPage >
                                    <#--true-->
                                    <li><a href="javascript:queryAll(1, ${page.pageSize});"><<</a></li>
                                    <li><a href="javascript:queryAll(${page.pageNum - 1}, ${page.pageSize});"><</a></li>
                                </#if>
                                <#--<#list page.navigatepageNums as navigatepageNum>-->
                                    <#--<#if navigatepageNum==page.pageNum>-->
                                        <#--<li class="active"><a href="javascript:queryAll(${navigatepageNum}, ${page.pageSize});">${navigatepageNum}</a></li>-->
                                    <#--</#if>-->
                                    <#--<#if navigatepageNum!=page.pageNum>-->
                                        <#--<li><a href="javascript:queryAll(${navigatepageNum}, ${page.pageSize});">${navigatepageNum}</a></li>-->
                                    <#--</#if>-->
                                <#--</#list>-->
                                <#if !page.isLastPage>
                                    <li>
                                        <a href="javascript:queryAll(${page.pageNum + 1}, ${page.pageSize});">></a>
                                    </li>
                                    <li>
                                        <a href="javascript:queryAll(${page.total}, ${page.pageSize});">>></a>
                                    </li>
                                </#if>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

        </section>
    </div>

</section>


<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.min.js"></script>
<script type="text/javascript">


    <#--console.log(${page.pageSize});-->
    function queryAll(pageNum, pageSize){
        document.location.href="/console/user/list?pageNum="+pageNum;
    }
</script>
</body>
</html>