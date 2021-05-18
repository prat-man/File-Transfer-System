// ResizeObserver polyfill for IE-11
if (typeof ResizeObserver === 'undefined') {
	function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }
	
	function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }
	
	function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }
	
	var ResizeObserver = /*#__PURE__*/function () {
	  function ResizeObserver(callback) {
	    _classCallCheck(this, ResizeObserver);
	
	    this.observables = []; // Array of observed elements that looks like this:
	    // [{
	    //   el: domNode,
	    //   size: {height: x, width: y}
	    // }]
	
	    this.boundCheck = this.check.bind(this);
	    this.boundCheck();
	    this.callback = callback;
	  }
	
	  _createClass(ResizeObserver, [{
	    key: "observe",
	    value: function observe(el) {
	      if (this.observables.some(function (observable) {
	        return observable.el === el;
	      })) {
	        return;
	      }
	
	      var newObservable = {
	        el: el,
	        size: {
	          height: el.clientHeight,
	          width: el.clientWidth
	        }
	      };
	      this.observables.push(newObservable);
	    }
	  }, {
	    key: "unobserve",
	    value: function unobserve(el) {
	      this.observables = this.observables.filter(function (obj) {
	        return obj.el !== el;
	      });
	    }
	  }, {
	    key: "disconnect",
	    value: function disconnect() {
	      this.observables = [];
	    }
	  }, {
	    key: "check",
	    value: function check() {
	      var changedEntries = this.observables.filter(function (obj) {
	        var currentHeight = obj.el.clientHeight;
	        var currentWidth = obj.el.clientWidth;
	
	        if (obj.size.height !== currentHeight || obj.size.width !== currentWidth) {
	          obj.size.height = currentHeight;
	          obj.size.width = currentWidth;
	          return true;
	        }
	      }).map(function (obj) {
	        return obj.el;
	      });
	
	      if (changedEntries.length > 0) {
	        this.callback(changedEntries);
	      }
	
	      window.requestAnimationFrame(this.boundCheck);
	    }
	  }]);
	
	  return ResizeObserver;
	}();
}

// show footer if IE
$(function(){
	if (isIE()) $('#footer').show();
	else $('#footer').remove();
});

// check if IE
function isIE() {
    var ua = window.navigator.userAgent;
    var msie = ua.indexOf("MSIE ");
    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) return true;
    return false;
}

// adjust content position with nav-bar height
$(function() {
	var height = $('#nav-bar-wrapper').height();
	$("#view-wrapper").css("padding-top", height + 20);
	
	var resizeObserver = new ResizeObserver(function(entries) {
		var height = $('#nav-bar-wrapper').height();
		$("#view-wrapper").css("padding-top", height + 20);
	});
	var target = document.querySelector('#nav-bar-wrapper');
	resizeObserver.observe(target);
});

// back button reload
window.addEventListener("pageshow", function(event) {
	var historyTraversal = event.persisted
			|| (typeof window.performance != "undefined" && window.performance.navigation.type === 2);
	if (historyTraversal) {
		window.location.reload();
	}
});