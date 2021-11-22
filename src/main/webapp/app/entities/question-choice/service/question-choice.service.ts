import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionChoice, getQuestionChoiceIdentifier } from '../question-choice.model';

export type EntityResponseType = HttpResponse<IQuestionChoice>;
export type EntityArrayResponseType = HttpResponse<IQuestionChoice[]>;

@Injectable({ providedIn: 'root' })
export class QuestionChoiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/question-choices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionChoice: IQuestionChoice): Observable<EntityResponseType> {
    return this.http.post<IQuestionChoice>(this.resourceUrl, questionChoice, { observe: 'response' });
  }

  update(questionChoice: IQuestionChoice): Observable<EntityResponseType> {
    return this.http.put<IQuestionChoice>(`${this.resourceUrl}/${getQuestionChoiceIdentifier(questionChoice) as number}`, questionChoice, {
      observe: 'response',
    });
  }

  partialUpdate(questionChoice: IQuestionChoice): Observable<EntityResponseType> {
    return this.http.patch<IQuestionChoice>(
      `${this.resourceUrl}/${getQuestionChoiceIdentifier(questionChoice) as number}`,
      questionChoice,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionChoice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionChoice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuestionChoiceToCollectionIfMissing(
    questionChoiceCollection: IQuestionChoice[],
    ...questionChoicesToCheck: (IQuestionChoice | null | undefined)[]
  ): IQuestionChoice[] {
    const questionChoices: IQuestionChoice[] = questionChoicesToCheck.filter(isPresent);
    if (questionChoices.length > 0) {
      const questionChoiceCollectionIdentifiers = questionChoiceCollection.map(
        questionChoiceItem => getQuestionChoiceIdentifier(questionChoiceItem)!
      );
      const questionChoicesToAdd = questionChoices.filter(questionChoiceItem => {
        const questionChoiceIdentifier = getQuestionChoiceIdentifier(questionChoiceItem);
        if (questionChoiceIdentifier == null || questionChoiceCollectionIdentifiers.includes(questionChoiceIdentifier)) {
          return false;
        }
        questionChoiceCollectionIdentifiers.push(questionChoiceIdentifier);
        return true;
      });
      return [...questionChoicesToAdd, ...questionChoiceCollection];
    }
    return questionChoiceCollection;
  }
}
