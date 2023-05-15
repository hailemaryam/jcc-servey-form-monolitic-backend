jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NewsService } from '../service/news.service';
import { INews, News } from '../news.model';

import { NewsUpdateComponent } from './news-update.component';

describe('News Management Update Component', () => {
  let comp: NewsUpdateComponent;
  let fixture: ComponentFixture<NewsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let newsService: NewsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NewsUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(NewsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NewsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    newsService = TestBed.inject(NewsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const news: INews = { id: 456 };

      activatedRoute.data = of({ news });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(news));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<News>>();
      const news = { id: 123 };
      jest.spyOn(newsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ news });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: news }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(newsService.update).toHaveBeenCalledWith(news);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<News>>();
      const news = new News();
      jest.spyOn(newsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ news });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: news }));
      saveSubject.complete();

      // THEN
      expect(newsService.create).toHaveBeenCalledWith(news);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<News>>();
      const news = { id: 123 };
      jest.spyOn(newsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ news });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(newsService.update).toHaveBeenCalledWith(news);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
