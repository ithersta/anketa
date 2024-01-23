import adapter from "@sveltejs/adapter-node";
import { vitePreprocess } from "@sveltejs/vite-plugin-svelte";

/** @type {import('@sveltejs/kit').Config} */
const config = {
    preprocess: [vitePreprocess({})],
    kit: {
        adapter: adapter(),
        csp: {
            directives: {
                "connect-src": [
                    "'self'",
                    "https://*.yandex.com",
                    "https://*.yandex.ru",
                    "https://*.yandex.md",
                ],
                "script-src": [
                    "'self'",
                    "https://yastatic.net",
                    "https://*.yandex.ru",
                    "https://*.yandex.com",
                ],
                "frame-src": ["https://*.yandex.ru"],
                "img-src": [
                    "'self'",
                    "https://*.yandex.ru",
                    "https://*.yandex.net",
                    "https://*.yandex.com",
                    "data:",
                ],
            },
        },
    },
};

export default config;
