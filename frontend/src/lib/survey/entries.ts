import type { MultiChoice } from "$lib/survey/multichoice";
import type { PolarChoice } from "$lib/survey/polarchoice";
import type { TextField } from "$lib/survey/textfield";
import type { Text } from "$lib/survey/text";

export type SurveyEntry = MultiChoice.Entry |
    PolarChoice.Entry |
    TextField.Entry |
    Text.Entry

export type SurveyAnswer = MultiChoice.Answer |
    PolarChoice.Answer |
    TextField.Answer