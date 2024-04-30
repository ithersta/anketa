<script>
  import "../app.postcss";
  import {ModeWatcher} from "mode-watcher";
  import {Toaster} from "$lib/components/ui/sonner";
</script>

<ModeWatcher defaultMode="light"/>
<Toaster />
<slot />
