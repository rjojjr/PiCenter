(this["webpackJsonppicenter-ui"]=this["webpackJsonppicenter-ui"]||[]).push([[0],{26:function(e,r,n){e.exports=n(56)},31:function(e,r,n){},32:function(e,r,n){},56:function(e,r,n){"use strict";n.r(r);var t=n(0),a=n.n(t),s=n(10),u=n.n(s),o=(n(31),n(32),n(3)),l=function(e){var r=e.isLoading,n=e.message;return a.a.createElement("div",null,r&&a.a.createElement("p",{className:"message"},n))},i=n(2),c=n.n(i),m=n(5),g=n(9),d=n.n(g),E=function(e){e.token;return d.a.get("/summary?userId="+e.token)},O=function(e,r){var n={username:e,password:r};return d.a.post("/login",n)},p=function(e){return{type:"LOGON_ERROR",msg:e}},y=function(e){return{type:"LOGGED_ON",user:e}},S=function(e,r){return{type:"SET_USER",user:e,loggedOn:r}},f=function(){return function(){var e=Object(m.a)(c.a.mark((function e(r){var n,t;return c.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.prev=0,e.next=3,d.a.get("/logout");case 3:n=e.sent,t=n.data.restUser,r(L(t)),e.next=10;break;case 8:e.prev=8,e.t0=e.catch(0);case 10:case"end":return e.stop()}}),e,null,[[0,8]])})));return function(r){return e.apply(this,arguments)}}()},L=function(e){return{type:"LOGOUT",user:e}},v=n(8),h=function(e){var r=e.logOn,n=e.message,s=e.user,u=e.isShowMsg,o=e.resetIsShowMsg,l=e.isLoggingOn,i=Object(t.useState)(""),c=Object(v.a)(i,2),m=c[0],g=c[1],d=Object(t.useState)(""),E=Object(v.a)(d,2),O=E[0],p=E[1];return a.a.createElement("div",{className:"page logOnPage"},l&&a.a.createElement("p",{className:"loading"},"Logging on...."),void 0!==s.username&&a.a.createElement(A,{user:s}),u&&a.a.createElement("p",{className:"message"},n),a.a.createElement("table",null,a.a.createElement("tbody",null,a.a.createElement("tr",null,a.a.createElement("td",null,a.a.createElement("p",null,"Username: ")),a.a.createElement("td",null,a.a.createElement("input",{type:"text",className:"logonInput",onChange:function(e){g(e.target.value),o()},value:m}))),a.a.createElement("tr",null,a.a.createElement("td",null,a.a.createElement("p",null,"Password: ")),a.a.createElement("td",null,a.a.createElement("input",{type:"password",className:"logonInput",onChange:function(e){p(e.target.value),o()},value:O}))))),a.a.createElement("button",{className:"submitLogin",type:"button",onClick:function(){r(m,O)}},"Logon"))},N={logOn:function(e,r){return function(){var n=Object(m.a)(c.a.mark((function n(t){var a,s;return c.a.wrap((function(n){for(;;)switch(n.prev=n.next){case 0:return n.prev=0,t({type:"LOGGING_ON"}),n.next=4,O(e,r);case 4:a=n.sent,"null"!==(s=a.data.restUser).userName?t(y(s)):t(p(a.data.responseBody.body)),n.next=12;break;case 9:n.prev=9,n.t0=n.catch(0),t(p("Try again later"));case 12:case"end":return n.stop()}}),n,null,[[0,9]])})));return function(e){return n.apply(this,arguments)}}()},resetIsShowMsg:function(){return{type:"RESET_IS_SHOW_MSG"}}},b=Object(o.b)((function(e){return{message:e.message,user:e.user,isShowMsg:e.isShowMsg,isLoggingOn:e.isLoggingOn}}),N)((function(e){var r=e.logOn,n=e.message,t=e.user,s=e.isShowMsg,u=e.resetIsShowMsg,o=e.isLoggingOn;return a.a.createElement("div",{className:"pageContainer logOnPage"},a.a.createElement("header",null,a.a.createElement("h2",{className:"lightText"},"PiCenter Logon")),a.a.createElement("div",{id:"main"},a.a.createElement("section",null,a.a.createElement(h,{user:t,logOn:r,resetIsShowMsg:u,isShowMsg:s,message:n,isLoggingOn:o})),a.a.createElement("nav",null,a.a.createElement("p",{className:"lightText"},"Sign in")),a.a.createElement("aside",null)),a.a.createElement("footer",null,a.a.createElement("a",{className:"lightText",href:"github.com/rjojjr"},"Visit me on github")))})),M=function(e){return{type:"SUMMARY_CAN_RENDER",canRender:e}},w=function(e){var r=e.summary,n=e.interval,s=e.index;return a.a.createElement(t.Fragment,null,a.a.createElement("tr",null,a.a.createElement("td",null,n),a.a.createElement("td",null,r.temps[s]),a.a.createElement("td",null,r.humiditys[s]),a.a.createElement("td",null,r.tempDevi[s]),a.a.createElement("td",null,r.humidityDevi[s])))},R=function(e){var r=e.summary,n=(e.user,e.isLoading);e.canRender;return a.a.createElement("div",{className:"page summaryPage"},!n&&a.a.createElement("div",null,a.a.createElement("h2",null,"Sensor ",r.roomName," Summary"),a.a.createElement("table",{className:"summaryTable"},a.a.createElement("tbody",null,a.a.createElement("tr",null,a.a.createElement("th",null,"Interval"),a.a.createElement("th",null,"Temperature Average"),a.a.createElement("th",null,"Humidity Average"),a.a.createElement("th",null,"Sample Standard Deviation")),a.a.createElement("tr",null,a.a.createElement("td",null),a.a.createElement("td",null),a.a.createElement("td",null),a.a.createElement("td",null,a.a.createElement("b",null,"Temp")),a.a.createElement("td",null,a.a.createElement("b",null,"Humidity"))),["Right Now","1 Hour","2 Hours","3 Hours","6 Hours","12 Hours","24 Hours"].map((function(e,n){return a.a.createElement(w,{interval:e,index:n,summary:r})}))))))},_=function(e){var r=e.selectSensor,n=e.summary,t=e.isLoading;return a.a.createElement("div",{className:"summaryHeader headerButtonContainer"},a.a.createElement("h3",null,"Sensors"),!t&&a.a.createElement("div",null,n.map((function(e,n){return a.a.createElement("button",{key:n,className:"summarySensorSelector",onClick:function(){r(n)}},e.roomName)}))))},j=function(e){var r=e.summary,n=e.user,s=(e.isError,e.errorMsg,e.isLoading),u=e.canLoad,o=e.logOff,i=Object(t.useState)(0),c=Object(v.a)(i,2),m=c[0],g=c[1];return a.a.createElement("div",{className:"container summaryPageContainer"},a.a.createElement(l,{isLoading:s,message:"Loading.."}),u&&!s&&a.a.createElement("div",null,a.a.createElement("header",null,a.a.createElement("h2",null,"PiCenter Sensor Summary")),a.a.createElement("div",{id:"main"},a.a.createElement("section",null,a.a.createElement("header",null,a.a.createElement(_,{isLoading:s,summary:r,selectSensor:function(e){g(e)}})),a.a.createElement(R,{canRender:u,isLoading:s,summary:0===r.length?"":r[m]})),a.a.createElement("nav",null,a.a.createElement("b",null,"Summary")),a.a.createElement("aside",null,a.a.createElement("h4",null,"Logged on as: ",n.userName),a.a.createElement("button",{type:"button",onClick:o},"Logout"))),a.a.createElement("footer",null,a.a.createElement("a",{href:"github.com/rjojjr"},"Visit me on github"))))},I={loadSummary:function(e){return function(){var r=Object(m.a)(c.a.mark((function r(n){var t;return c.a.wrap((function(r){for(;;)switch(r.prev=r.next){case 0:return r.prev=0,n({type:"SUMMARY_LOADING"}),r.next=4,E(e);case 4:t=r.sent,n({type:"SET_SUMMARY",summary:t.data.summary}),n({type:"SUMMARY_DONE_LOADING"}),n(M(!0)),r.next=15;break;case 10:r.prev=10,r.t0=r.catch(0),n(M(!1)),n({type:"SUMMARY_LOADING_ERROR",msg:"Network error"});case 15:case"end":return r.stop()}}),r,null,[[0,10]])})));return function(e){return r.apply(this,arguments)}}()},logOff:f},G=Object(o.b)((function(e){return{summary:e.summary,user:e.user,isLoading:e.isSummaryLoading,isError:e.isSummaryError,errorMsg:e.message,canLoad:e.canRenderSummary}}),I)((function(e){var r=e.loadSummary,n=e.summary,t=e.user,s=e.isLoading,u=e.isError,o=e.errorMsg,l=e.canLoad,i=e.logOff;return s&&r(t),a.a.createElement("div",null,u&&a.a.createElement("p",null,"An error has happened: ",o),a.a.createElement(j,{summary:n,user:t,isLoading:s,canLoad:l,logOff:i}))})),A=function(e){var r=e.user.page;return a.a.createElement("div",{className:"pageSelector"},"/login"===r&&a.a.createElement(b,null),"/summary"===r&&a.a.createElement(G,null))},D=Object(o.b)((function(e){return{user:e.user}}),void 0)((function(e){var r=e.user;return a.a.createElement("div",{className:"appContainer"},r!=={}&&"null"!==r.userName&&void 0!==r.userName&&a.a.createElement(A,{user:r}),r==={}||"null"===r.userName&&a.a.createElement(A,{user:{page:"/login"}}))})),T={loadApp:function(){return function(){var e=Object(m.a)(c.a.mark((function e(r){var n,t;return c.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.prev=0,r({type:"IS_LOADING"}),e.next=4,d.a.get("/loading");case 4:return n=e.sent,t=n.data.restUser,r(S(t)),r({type:"IS_NOT_LOADING"}),e.abrupt("return");case 11:e.prev=11,e.t0=e.catch(0),r({type:"LOADING_ERROR",msg:"Network error"});case 15:case"end":return e.stop()}}),e,null,[[0,11]])})));return function(r){return e.apply(this,arguments)}}()}},k=Object(o.b)((function(e){return{isLoading:e.isLoading}}),T)((function(e){e.isError;var r=e.loadApp,n=(e.errorMsg,e.isLoading);return Object(t.useEffect)((function(){r()}),[r]),a.a.createElement("div",null,a.a.createElement(l,{isLoading:n,message:"Loading..."}),!n&&a.a.createElement(D,null))})),U=n(7),x=n(24),P=n(25);function C(e,r){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var t=Object.getOwnPropertySymbols(e);r&&(t=t.filter((function(r){return Object.getOwnPropertyDescriptor(e,r).enumerable}))),n.push.apply(n,t)}return n}function H(e){for(var r=1;r<arguments.length;r++){var n=null!=arguments[r]?arguments[r]:{};r%2?C(n,!0).forEach((function(r){Object(P.a)(e,r,n[r])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):C(n).forEach((function(r){Object.defineProperty(e,r,Object.getOwnPropertyDescriptor(n,r))}))}return e}var Y=window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__||U.c,B=Object(U.d)((function(){var e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{user:{},isLoadingError:!1,loadingErrorMsg:"",isLoading:!1,isError:!1,message:"",isShowMsg:!1,errorMsg:"",isLoggedOn:!1,isLoggingOn:!1,summary:[],isSummaryLoading:!0,isSummaryError:!1,loaded:!1,canRenderSummary:!1},r=arguments.length>1&&void 0!==arguments[1]?arguments[1]:{type:void 0};switch(r.type){case"RESET_IS_ERROR":return H({},e,{isError:!1,errorMsg:""});case"RESET_IS_SHOW_MSG":return H({},e,{isShowMsg:!1,message:""});case"IS_LOADING":return H({},e,{user:{},isLoading:!0,isShowMsg:!1,isError:!1,isLoggedOn:!1,loaded:!0});case"IS_NOT_LOADING":return H({},e,{isLoading:!1,isShowMsg:!1,isError:!1,loaded:!0});case"NOT_LOGGED_ON":return H({},e,{user:{},isLoading:!1,isError:!1,isShowMsg:!0,message:r.msg,isLoggedOn:!1});case"LOADING_ERROR":return H({},e,{user:{},isLoading:!1,isLoadingError:!0,loadingErrorMsg:r.msg,isLoggedOn:!1});case"LOGON_ERROR":return H({},e,{user:{},isLoggingOn:!1,isShowMsg:!0,message:r.msg,isLoggedOn:!1});case"LOGGING_ON":return H({},e,{isLoggingOn:!0,isShowMsg:!1,isError:!1,isLoggedOn:!1});case"LOGGED_ON":return H({},e,{user:r.user,isLoggedOn:!0});case"SET_USER":var n=function(){return"null"!==r.user.userName};return H({},e,{user:r.user,isLoggedOn:n()});case"SET_SUMMARY":return H({},e,{summary:r.summary,isSummaryError:!1});case"SUMMARY_CAN_RENDER":return H({},e,{canRenderSummary:r.canRender});case"SUMMARY_LOADING_ERROR":return H({},e,{isSummaryError:!0,isSummaryLoading:!1,isShowMsg:!0,message:r.msg});case"SUMMARY_LOADING":return H({},e,{isSummaryError:!1,isSummaryLoading:!0,isShowMsg:!1,canRenderSummary:!1});case"SUMMARY_DONE_LOADING":return H({},e,{isSummaryError:!1,isSummaryLoading:!1,isShowMsg:!1});case"LOGOUT":return H({},e,{user:r.user,isLoadingError:!1,loadingErrorMsg:"",isLoading:!1,isError:!1,message:"",isShowMsg:!1,errorMsg:"",isLoggedOn:!1,isLoggingOn:!1,summary:[],isSummaryLoading:!0,isSummaryError:!1,loaded:!1,canRenderSummary:!1});default:return e}}),Y(Object(U.a)(x.a)));var W=function(){return a.a.createElement(o.a,{store:B},a.a.createElement(k,null))};Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));u.a.render(a.a.createElement(W,null),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then((function(e){e.unregister()}))}},[[26,1,2]]]);
//# sourceMappingURL=main.418f9567.chunk.js.map