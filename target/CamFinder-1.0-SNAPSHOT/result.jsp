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
</head>
<body>

${name}
${price}
${megapixel}
${zoom}
${storage_mode}
${sensitivity}
${shutter_speed}
${sensor_size}

<div class="container">
    <form method="post" action="/">
        <c:forEach var="url" items="${urls}">
            <div class="form-group">
                <label for="${attribute.getKey()}Input">${attribute.getKey()}</label>
                <input type="text" class="form-control" id="${attribute.getKey()}Input"
                       name="${attribute.getKey()}">
            </div>
        </c:forEach>
    </form>
</div>

<a href="/">Submit another search</a>
</body>
</html>
