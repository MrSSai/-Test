<#setting classic_compatible=true>
<#assign base=request.contextPath />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>公告列表</title>
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
                                <th>Title</th>
                                <th>内容</th>
                                <th>状态</th>
                                <th>创建时间</th>
                                <th>更新时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list notices as noti>
                            <tr>
                                <td> ${noti.id}</td>
                                <td> ${noti.title}</td>
                                <td> ${noti.content}</td>
                                <#if noti.status ==0>
                                    <td>草稿</td>
                                <#else>
                                    <td>发布</td>
                                </#if>
                                <td> ${noti.createTime?string('yyyy-MM-dd')}</td>
                                <td> ${noti.updateTime?string('yyyy-MM-dd')}</td>
                            </tr>
                            </#list>
                            </tr>
                            </tbody>
                        </table>

                    </div>
                </div>

                <#--<div class="row">-->
                    <#--<div class="col-xs-6">-->
                        <#--<div class="message">-->
                            <#--共<i class="blue">${page.total}</i>条记录，总页数：${page.pages}  当前显示第 <i-->
                                <#--class="blue">${page.pageNum}/${page.pages}</i> 页-->
                        <#--</div>-->
                    <#--</div>-->
                    <#--<div class="col-xs-6">-->
                        <#--<div class="dataTables_paginate paging_bootstrap">-->
                            <#--<ul class="pagination">-->
                            <#--<#if !page.isFirstPage >-->
                            <#--&lt;#&ndash;true&ndash;&gt;-->
                                <#--<li><a href="javascript:queryAll(1, ${page.pageSize});"><<</a></li>-->
                                <#--<li><a href="javascript:queryAll(${page.pageNum - 1}, ${page.pageSize});"><</a></li>-->
                            <#--</#if>-->
                            <#--&lt;#&ndash;<#list page.navigatepageNums as navigatepageNum>&ndash;&gt;-->
                            <#--&lt;#&ndash;<#if navigatepageNum==page.pageNum>&ndash;&gt;-->
                            <#--&lt;#&ndash;<li class="active"><a href="javascript:queryAll(${navigatepageNum}, ${page.pageSize});">${navigatepageNum}</a></li>&ndash;&gt;-->
                            <#--&lt;#&ndash;</#if>&ndash;&gt;-->
                            <#--&lt;#&ndash;<#if navigatepageNum!=page.pageNum>&ndash;&gt;-->
                            <#--&lt;#&ndash;<li><a href="javascript:queryAll(${navigatepageNum}, ${page.pageSize});">${navigatepageNum}</a></li>&ndash;&gt;-->
                            <#--&lt;#&ndash;</#if>&ndash;&gt;-->
                            <#--&lt;#&ndash;</#list>&ndash;&gt;-->
                            <#--<#if !page.isLastPage>-->
                                <#--<li>-->
                                    <#--<a href="javascript:queryAll(${page.pageNum + 1}, ${page.pageSize});">></a>-->
                                <#--</li>-->
                                <#--<li>-->
                                    <#--<a href="javascript:queryAll(${page.total}, ${page.pageSize});">>></a>-->
                                <#--</li>-->
                            <#--</#if>-->
                            <#--</ul>-->
                        <#--</div>-->
                    <#--</div>-->
                <#--</div>-->
            <#--</div>-->
            <script type="text/javascript">


                console.log(${page.pageSize});
                function queryAll(pageNum, pageSize){
                    document.location.href="/console/user/list?pageNum="+pageNum;
                }
            </script>
        </section>
    </div>

</section>


<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.min.js"></script>

</body>
</html>


