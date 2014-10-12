<!DOCTYPE html>
<html>

    <head>
        <title>HTTP Live Streaming Test Player</title>
        <script src="${pageContext.request.contextPath}/libs/shim/js/console.time.js"></script>
    </head>
    <body class="loading" style="text-align: center">
        <main>
            <div id="video-streaming">
                <canvas id="canvas" width="398" height="224"></canvas>
            </div>
            <br>
            <input class="button orange" type="button" id="play" value="Play">
            <input class="button black" type="button" id="pause" value="Pause">
        </main>
        <img id="loading-indicator" src="${pageContext.request.contextPath}/img/ajax-loader.gif">
        <script src="${pageContext.request.contextPath}/libs/browser/js/browser.js" data-canvas="canvas" data-hls="${pageContext.request.contextPath}/webcams/${webcamId}/playlist">
        		// UI initialization when first video chunk is converted

        		document.body.classList.remove('loading');

        		var converter = this;

        		['play', 'pause'].forEach(function (action) {
        			document.getElementById(action).addEventListener('click', function () {
        				document.body.classList.toggle('paused');
        				converter.currentVideo[action]();
        			});
        		});
        </script>
    </body>

</html>