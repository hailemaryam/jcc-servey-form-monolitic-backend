jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MultipleChoiceAnsewerService } from '../service/multiple-choice-ansewer.service';

import { MultipleChoiceAnsewerDeleteDialogComponent } from './multiple-choice-ansewer-delete-dialog.component';

describe('MultipleChoiceAnsewer Management Delete Component', () => {
  let comp: MultipleChoiceAnsewerDeleteDialogComponent;
  let fixture: ComponentFixture<MultipleChoiceAnsewerDeleteDialogComponent>;
  let service: MultipleChoiceAnsewerService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MultipleChoiceAnsewerDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(MultipleChoiceAnsewerDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MultipleChoiceAnsewerDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MultipleChoiceAnsewerService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
