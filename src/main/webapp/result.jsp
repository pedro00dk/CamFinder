<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Guilherme
  Date: 23/06/2017
  Time: 01:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Result</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>

<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Price</th>
            <th>Sensitivity</th>
            <th>Shutter Speed</th>
            <th>Storage Mode</th>
            <th>URL</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="mappedUrl" items="${mappedUrls}">
            <tr>
                <th>${mappedUrl.getValue().getOrDefault("name","")}</th>
                <th>${mappedUrl.getValue().getOrDefault("price","")}</th>
                <th>${mappedUrl.getValue().getOrDefault("Sensitivity","")}</th>
                <th>${mappedUrl.getValue().getOrDefault("Shutter Speed","")}</th>
                <th>${mappedUrl.getValue().getOrDefault("Storage Mode","")}</th>
                <td scope="row"><a target="_blank" href="${mappedUrl.getKey()}">SITE</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

   <a class="brand" href="/">Submit another search</a>
</div>
</body>
</html>
