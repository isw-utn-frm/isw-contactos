import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TestTestModule } from '../../../test.module';
import { ContactoDeleteDialogComponent } from 'app/entities/contacto/contacto-delete-dialog.component';
import { ContactoService } from 'app/entities/contacto/contacto.service';

describe('Component Tests', () => {
  describe('Contacto Management Delete Component', () => {
    let comp: ContactoDeleteDialogComponent;
    let fixture: ComponentFixture<ContactoDeleteDialogComponent>;
    let service: ContactoService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TestTestModule],
        declarations: [ContactoDeleteDialogComponent]
      })
        .overrideTemplate(ContactoDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContactoDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContactoService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
