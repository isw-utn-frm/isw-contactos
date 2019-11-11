import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TestTestModule } from '../../../test.module';
import { ContactoUpdateComponent } from 'app/entities/contacto/contacto-update.component';
import { ContactoService } from 'app/entities/contacto/contacto.service';
import { Contacto } from 'app/shared/model/contacto.model';

describe('Component Tests', () => {
  describe('Contacto Management Update Component', () => {
    let comp: ContactoUpdateComponent;
    let fixture: ComponentFixture<ContactoUpdateComponent>;
    let service: ContactoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [ContactoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ContactoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContactoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContactoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Contacto(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Contacto();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
