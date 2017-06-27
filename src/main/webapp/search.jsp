<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cam Finder</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>

<div class="container">
    <form method="post" action="/">
        <div class="form-group">
            <label for="nameInput">Name</label>
            <input type="text" class="form-control" id="nameInput" name="name" placeholder="Put name here">
        </div>
        <div class="form-group">
            <label for="priceInput">Price</label>
            <input type="text" class="form-control" id="priceInput" name="price" placeholder="Put price here">
        </div>
        <div class="form-group">
            <label for="megapixelInput">Megapixel</label>
            <input type="text" class="form-control" id="megapixelInput" name="megapixel" placeholder="Put mb here">
        </div>
        <div class="form-group">
            <label for="zoomInput">Zoom</label>
            <input type="text" class="form-control" id="zoomInput" name="zoom" placeholder="Put zoom here">
        </div>
        <div class="form-group">
            <label for="storage_modeInput">Storage Mode</label>
            <input type="text" class="form-control" id="storage_modeInput" name="storage_mode"
                   placeholder="Put SM here">
        </div>
        <div class="form-group">
            <label for="sensitivityInput">Sensitivity</label>
            <input type="text" class="form-control" id="sensitivityInput" name="sensitivity"
                   placeholder="Put sensitivity here">
        </div>
        <div class="form-group">
            <label for="shutter_speedInput">Shutter Speed</label>
            <input type="text" class="form-control" id="shutter_speedInput" name="shutter_speed"
                   placeholder="Put shutter speed here">
        </div>
        <div class="form-group">
            <label for="sensor_sizeInput">Sensor Size</label>
            <input type="text" class="form-control" id="sensor_sizeInput" name="sensor_size"
                   placeholder="Put sensor size here">
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>

</body>
</html>