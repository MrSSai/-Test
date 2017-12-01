<#setting classic_compatible=true>
<#assign base=request.contextPath />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>角色列表</title>
    <link href="${base}/css/viewer.css" rel="stylesheet" type="text/css"/>
    <link href=" ${base}/css/bootstrap.min.css" rel="stylesheet">
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
                                <th>描述</th>
                                <th>创建时间</th>
                                <th>更新时间</th>

                            </tr>
                            </thead>
                            <tbody>
                                <#if page.list??>
                                    <#list page.list as item>
                                    <tr scope="row">
                                        <td>${item.id}</td>
                                        <td>${item.name}</td>
                                        <td>${item.descrtption}</td>
                                        <td>${(item.createTime?string('yyyy-mm-dd'))!'无数据'}</td>
                                        <td>${(item.updateTime?string('yyyy-mm-dd'))!}}</td>
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

                                <#if !page.isLastPage>
                                    <li>
                                        <a href="javascript:queryAll(${page.nextPage}, ${page.pageSize});">></a>
                                    </li>
                                    <li>
                                        <a href="javascript:queryAll(${page.lastPage}, ${page.pageSize});">>></a>
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
    function queryAll(pageNum, pageSize){
        document.location.href="/roles/list?pageNum="+pageNum;
    }
</script>
</body>
</html>