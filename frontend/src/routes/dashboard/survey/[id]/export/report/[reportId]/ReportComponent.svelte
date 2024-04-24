<script lang="ts">
    import type { SurveyContent } from "$lib/survey/survey";
    import { getShortName, type SurveyEntry } from "$lib/survey/entries";
    import type { ReportDraftEntry } from "$lib/report/draft";

    export let entry: ReportDraftEntry
    export let survey: SurveyContent

    const surveyEntry: SurveyEntry = survey.entries.find(e => e.id === entry.content["forEntryWithId"])
</script>

{#if entry.content.type === "Text"}
    {entry.content.content}
{:else}
    {getShortName(surveyEntry)}
{/if}
