import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMultipleChoiceAnsewer, MultipleChoiceAnsewer } from '../multiple-choice-ansewer.model';

import { MultipleChoiceAnsewerService } from './multiple-choice-ansewer.service';

describe('MultipleChoiceAnsewer Service', () => {
  let service: MultipleChoiceAnsewerService;
  let httpMock: HttpTestingController;
  let elemDefault: IMultipleChoiceAnsewer;
  let expectedResult: IMultipleChoiceAnsewer | IMultipleChoiceAnsewer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MultipleChoiceAnsewerService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      choice: 'AAAAAAA',
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

    it('should create a MultipleChoiceAnsewer', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MultipleChoiceAnsewer()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MultipleChoiceAnsewer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          choice: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MultipleChoiceAnsewer', () => {
      const patchObject = Object.assign(
        {
          choice: 'BBBBBB',
        },
        new MultipleChoiceAnsewer()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MultipleChoiceAnsewer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          choice: 'BBBBBB',
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

    it('should delete a MultipleChoiceAnsewer', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMultipleChoiceAnsewerToCollectionIfMissing', () => {
      it('should add a MultipleChoiceAnsewer to an empty array', () => {
        const multipleChoiceAnsewer: IMultipleChoiceAnsewer = { id: 123 };
        expectedResult = service.addMultipleChoiceAnsewerToCollectionIfMissing([], multipleChoiceAnsewer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(multipleChoiceAnsewer);
      });

      it('should not add a MultipleChoiceAnsewer to an array that contains it', () => {
        const multipleChoiceAnsewer: IMultipleChoiceAnsewer = { id: 123 };
        const multipleChoiceAnsewerCollection: IMultipleChoiceAnsewer[] = [
          {
            ...multipleChoiceAnsewer,
          },
          { id: 456 },
        ];
        expectedResult = service.addMultipleChoiceAnsewerToCollectionIfMissing(multipleChoiceAnsewerCollection, multipleChoiceAnsewer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MultipleChoiceAnsewer to an array that doesn't contain it", () => {
        const multipleChoiceAnsewer: IMultipleChoiceAnsewer = { id: 123 };
        const multipleChoiceAnsewerCollection: IMultipleChoiceAnsewer[] = [{ id: 456 }];
        expectedResult = service.addMultipleChoiceAnsewerToCollectionIfMissing(multipleChoiceAnsewerCollection, multipleChoiceAnsewer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(multipleChoiceAnsewer);
      });

      it('should add only unique MultipleChoiceAnsewer to an array', () => {
        const multipleChoiceAnsewerArray: IMultipleChoiceAnsewer[] = [{ id: 123 }, { id: 456 }, { id: 82129 }];
        const multipleChoiceAnsewerCollection: IMultipleChoiceAnsewer[] = [{ id: 123 }];
        expectedResult = service.addMultipleChoiceAnsewerToCollectionIfMissing(
          multipleChoiceAnsewerCollection,
          ...multipleChoiceAnsewerArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const multipleChoiceAnsewer: IMultipleChoiceAnsewer = { id: 123 };
        const multipleChoiceAnsewer2: IMultipleChoiceAnsewer = { id: 456 };
        expectedResult = service.addMultipleChoiceAnsewerToCollectionIfMissing([], multipleChoiceAnsewer, multipleChoiceAnsewer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(multipleChoiceAnsewer);
        expect(expectedResult).toContain(multipleChoiceAnsewer2);
      });

      it('should accept null and undefined values', () => {
        const multipleChoiceAnsewer: IMultipleChoiceAnsewer = { id: 123 };
        expectedResult = service.addMultipleChoiceAnsewerToCollectionIfMissing([], null, multipleChoiceAnsewer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(multipleChoiceAnsewer);
      });

      it('should return initial array if no MultipleChoiceAnsewer is added', () => {
        const multipleChoiceAnsewerCollection: IMultipleChoiceAnsewer[] = [{ id: 123 }];
        expectedResult = service.addMultipleChoiceAnsewerToCollectionIfMissing(multipleChoiceAnsewerCollection, undefined, null);
        expect(expectedResult).toEqual(multipleChoiceAnsewerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
