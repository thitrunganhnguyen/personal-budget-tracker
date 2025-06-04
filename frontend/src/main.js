import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import 'bootstrap/dist/css/bootstrap.min.css';

const app = createApp(App)


// Deaktiviere die Vue Devtools In-App-Komponenten-Overlay
if (window.__VUE_DEVTOOLS_GLOBAL_HOOK__) {
  window.__VUE_DEVTOOLS_GLOBAL_HOOK__.emit = () => {};
}


app.use(router)

app.mount('#app')
