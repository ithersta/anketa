<script lang="ts">
    import * as Card from "$lib/components/ui/card";

    import { Button } from "$lib/components/ui/button";
    import {
        CheckCheck, CircleDot, HelpCircle, Ruler, TextCursorInput, TextIcon,
    } from "lucide-svelte";
    import type { SurveyEntryBuilderUiState } from "$lib/survey/entries";
    import { Text } from "$lib/survey/text";
    import { TextField } from "$lib/survey/textfield";
    import { MultiChoice } from "$lib/survey/multichoice";
    import { PolarChoice } from "$lib/survey/polarchoice";
    import type { ReportEntryUiState } from "$lib/report/entries";
    import { TextReport } from "$lib/report/text";

    export let openEditor: (uiState: ReportEntryUiState) => void
    export let openChooseNewEntryDialog: () => void
</script>

<Card.Root>
    <Card.Header>
        <Card.Title>Добавить блок</Card.Title>
    </Card.Header>
    <Card.Content>
        <div class="grid grid-cols-1 gap-2 md:grid-cols-2">
            <Button variant="secondary" on:click={() => openEditor(TextReport.toUiState(undefined))}>
                <TextIcon class="mr-2 h-4 w-4" />
                Текст
            </Button>
            <Button variant="secondary" on:click={openChooseNewEntryDialog}>
                <HelpCircle class="mr-2 h-4 w-4" />
                Ответы на вопрос
            </Button>
        </div>
    </Card.Content>
</Card.Root>
