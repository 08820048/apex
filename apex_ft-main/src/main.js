import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "./style.css";

const app = createApp(App);

// ????????
app.config.errorHandler = (err, _vm, info) => {
  console.error("Vue??????:", err, info);
};

// ???��?????Promise??????
window.addEventListener("unhandledrejection", (event) => {
  console.error("��?????Promise????:", event.reason);
  event.preventDefault();
});

app.use(router);
app.mount("#app");
