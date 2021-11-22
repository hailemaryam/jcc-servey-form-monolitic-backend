import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeOfOrganization, TypeOfOrganization } from '../type-of-organization.model';

import { TypeOfOrganizationService } from './type-of-organization.service';

describe('TypeOfOrganization Service', () => {
  let service: TypeOfOrganizationService;
  let httpMock: HttpTestingController;
  let elemDefault: ITypeOfOrganization;
  let expectedResult: ITypeOfOrganization | ITypeOfOrganization[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeOfOrganizationService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a TypeOfOrganization', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TypeOfOrganization()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeOfOrganization', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeOfOrganization', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
        },
        new TypeOfOrganization()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeOfOrganization', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a TypeOfOrganization', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTypeOfOrganizationToCollectionIfMissing', () => {
      it('should add a TypeOfOrganization to an empty array', () => {
        const typeOfOrganization: ITypeOfOrganization = { id: 123 };
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing([], typeOfOrganization);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeOfOrganization);
      });

      it('should not add a TypeOfOrganization to an array that contains it', () => {
        const typeOfOrganization: ITypeOfOrganization = { id: 123 };
        const typeOfOrganizationCollection: ITypeOfOrganization[] = [
          {
            ...typeOfOrganization,
          },
          { id: 456 },
        ];
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing(typeOfOrganizationCollection, typeOfOrganization);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeOfOrganization to an array that doesn't contain it", () => {
        const typeOfOrganization: ITypeOfOrganization = { id: 123 };
        const typeOfOrganizationCollection: ITypeOfOrganization[] = [{ id: 456 }];
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing(typeOfOrganizationCollection, typeOfOrganization);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeOfOrganization);
      });

      it('should add only unique TypeOfOrganization to an array', () => {
        const typeOfOrganizationArray: ITypeOfOrganization[] = [{ id: 123 }, { id: 456 }, { id: 74459 }];
        const typeOfOrganizationCollection: ITypeOfOrganization[] = [{ id: 123 }];
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing(typeOfOrganizationCollection, ...typeOfOrganizationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeOfOrganization: ITypeOfOrganization = { id: 123 };
        const typeOfOrganization2: ITypeOfOrganization = { id: 456 };
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing([], typeOfOrganization, typeOfOrganization2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeOfOrganization);
        expect(expectedResult).toContain(typeOfOrganization2);
      });

      it('should accept null and undefined values', () => {
        const typeOfOrganization: ITypeOfOrganization = { id: 123 };
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing([], null, typeOfOrganization, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeOfOrganization);
      });

      it('should return initial array if no TypeOfOrganization is added', () => {
        const typeOfOrganizationCollection: ITypeOfOrganization[] = [{ id: 123 }];
        expectedResult = service.addTypeOfOrganizationToCollectionIfMissing(typeOfOrganizationCollection, undefined, null);
        expect(expectedResult).toEqual(typeOfOrganizationCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
