
(function(d, t) {
      var v = d.createElement(t), s = d.getElementsByTagName(t)[0];
      v.onload = function() {
        window.voiceflow.chat.load({
          verify: { projectID: '6966fdfb018e0ecf9503a11f' },
          url: 'https://general-runtime.voiceflow.com/',
          versionID: 'production',
          voice: {
            url: "https://runtime-api.voiceflow.com/"
          }
        });
      }
      v.src = "https://cdn.voiceflow.com/widget-next/bundle.mjs"; v.type = "text/javascript"; s.parentNode.insertBefore(v, s);
  })(document, 'script');