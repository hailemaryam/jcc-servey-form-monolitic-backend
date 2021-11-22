import { IAnswer } from 'app/entities/answer/answer.model';

export interface IMultipleChoiceAnsewer {
  id?: number;
  choice?: string | null;
  answer?: IAnswer | null;
}

export class MultipleChoiceAnsewer implements IMultipleChoiceAnsewer {
  constructor(public id?: number, public choice?: string | null, public answer?: IAnswer | null) {}
}

export function getMultipleChoiceAnsewerIdentifier(multipleChoiceAnsewer: IMultipleChoiceAnsewer): number | undefined {
  return multipleChoiceAnsewer.id;
}
