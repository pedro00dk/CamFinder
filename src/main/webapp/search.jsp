<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cam Finder</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>

<div class="container">
    <form method="post" action="/">
        <c:forEach var="attribute" items="${attributes}">
            <div class="form-group">
                <label for="${attribute}Input">${attribute}</label>
                <input type="text" class="form-control" id="${attribute}Input" name="${attribute}">
            </div>
        </c:forEach>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>

</body>
</html>