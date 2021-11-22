import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuestionChoice, QuestionChoice } from '../question-choice.model';

import { QuestionChoiceService } from './question-choice.service';

describe('QuestionChoice Service', () => {
  let service: QuestionChoiceService;
  let httpMock: HttpTestingController;
  let elemDefault: IQuestionChoice;
  let expectedResult: IQuestionChoice | IQuestionChoice[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuestionChoiceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      option: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a QuestionChoice', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new QuestionChoice()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a QuestionChoice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          option: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a QuestionChoice', () => {
      const patchObject = Object.assign(
        {
          option: 'BBBBBB',
        },
        new QuestionChoice()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of QuestionChoice', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          option: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a QuestionChoice', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addQuestionChoiceToCollectionIfMissing', () => {
      it('should add a QuestionChoice to an empty array', () => {
        const questionChoice: IQuestionChoice = { id: 123 };
        expectedResult = service.addQuestionChoiceToCollectionIfMissing([], questionChoice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionChoice);
      });

      it('should not add a QuestionChoice to an array that contains it', () => {
        const questionChoice: IQuestionChoice = { id: 123 };
        const questionChoiceCollection: IQuestionChoice[] = [
          {
            ...questionChoice,
          },
          { id: 456 },
        ];
        expectedResult = service.addQuestionChoiceToCollectionIfMissing(questionChoiceCollection, questionChoice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a QuestionChoice to an array that doesn't contain it", () => {
        const questionChoice: IQuestionChoice = { id: 123 };
        const questionChoiceCollection: IQuestionChoice[] = [{ id: 456 }];
        expectedResult = service.addQuestionChoiceToCollectionIfMissing(questionChoiceCollection, questionChoice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionChoice);
      });

      it('should add only unique QuestionChoice to an array', () => {
        const questionChoiceArray: IQuestionChoice[] = [{ id: 123 }, { id: 456 }, { id: 13639 }];
        const questionChoiceCollection: IQuestionChoice[] = [{ id: 123 }];
        expectedResult = service.addQuestionChoiceToCollectionIfMissing(questionChoiceCollection, ...questionChoiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const questionChoice: IQuestionChoice = { id: 123 };
        const questionChoice2: IQuestionChoice = { id: 456 };
        expectedResult = service.addQuestionChoiceToCollectionIfMissing([], questionChoice, questionChoice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionChoice);
        expect(expectedResult).toContain(questionChoice2);
      });

      it('should accept null and undefined values', () => {
        const questionChoice: IQuestionChoice = { id: 123 };
        expectedResult = service.addQuestionChoiceToCollectionIfMissing([], null, questionChoice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionChoice);
      });

      it('should return initial array if no QuestionChoice is added', () => {
        const questionChoiceCollection: IQuestionChoice[] = [{ id: 123 }];
        expectedResult = service.addQuestionChoiceToCollectionIfMissing(questionChoiceCollection, undefined, null);
        expect(expectedResult).toEqual(questionChoiceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
