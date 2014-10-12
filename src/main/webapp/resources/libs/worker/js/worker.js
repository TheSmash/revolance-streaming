'use strict';

importScripts('../../../libs/require/js/require.js');

require.config({
	paths: {
		jdataview: '//jdataview.github.io/dist/jdataview',
		jbinary: '//jdataview.github.io/dist/jbinary',
		async: '../../../libs/async/js/async',
		consoleTime: '../../../libs/shim/js/console.time',
		consoleWorker: '../../../libs/shim/js/console.worker'
	},
	shim: {
		consoleTime: {
			deps: ['consoleWorker'],
			exports: 'console'
		},
		consoleWorker: {
			deps: [],
			exports: 'console'
		}
	}
});

require(['async', 'jbinary', '../../../libs/mpegts/js/mpegts', '../../../libs/mpegts/js/index', 'consoleTime', 'consoleWorker'],
	function (async, jBinary, MPEGTS, mpegts_to_mp4) {
		addEventListener('message', function (event) {
			// processing received sources one by one
			async.eachSeries(event.data, function (msg, callback) {
				jBinary.load(msg.url, MPEGTS, function (err, mpegts) {
					// tell async we can load next one
					callback(err);
					if (err) return;

					console.time('convert');
					var mp4 = mpegts_to_mp4(mpegts);
					console.timeEnd('convert');

					postMessage({
						type: 'video',
						index: msg.index,
						original: msg.url,
						url: mp4.toURI('video/mp4')
					});
				});
			});
		});

		postMessage({type: 'ready'});
	}
);