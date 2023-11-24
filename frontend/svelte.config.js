import adapter from "@sveltejs/adapter-auto";
import { vitePreprocess } from "@sveltejs/kit/vite";

/** @type {import('@sveltejs/kit').Config} */
const config = {
  // Consult https://kit.svelte.dev/docs/integrations#preprocessors
  // for more information about preprocessors
  preprocess: [vitePreprocess({})],

  kit: {
    // adapter-auto only supports some environments, see https://kit.svelte.dev/docs/adapter-auto for a list.
    // If your environment is not supported or you settled on a specific environment, switch out the adapter.
    // See https://kit.svelte.dev/docs/adapters for more information about adapters.
    adapter: adapter(),
    csp: {
      directives: {
        "script-src": [
          "'self'",
          "https://yastatic.net",
          "https://mc.yandex.ru",
        ],
        "frame-src": ["https://autofill.yandex.ru"],
        "img-src": [
          "'self'",
          "https://passport-rc.yandex.ru",
          "https://avatars.mds.yandex.net",
          "data:",
        ],
      },
    },
  },
};

export default config;
