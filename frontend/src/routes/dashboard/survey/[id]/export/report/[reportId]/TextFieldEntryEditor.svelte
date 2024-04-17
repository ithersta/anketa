<script lang="ts">
    import * as Dialog from "$lib/components/ui/dialog";
    import { TextFieldReport } from "$lib/report/textfield";
    import type { SurveyEntry } from "$lib/survey/entries";
    import { TextField } from "$lib/survey/textfield";

    export let uiState: TextFieldReport.UiState
    export let surveyEntry: TextField.Entry
    export let forceError: boolean
    const { content, hints } = uiState
</script>

<Dialog.Header>
    <Dialog.Title>{surveyEntry.question}</Dialog.Title>
    <Dialog.Description>Текстовое поле</Dialog.Description>
</Dialog.Header>
