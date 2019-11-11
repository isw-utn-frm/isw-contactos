import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContacto } from 'app/shared/model/contacto.model';
import { ContactoService } from './contacto.service';

@Component({
  selector: 'jhi-contacto-delete-dialog',
  templateUrl: './contacto-delete-dialog.component.html'
})
export class ContactoDeleteDialogComponent {
  contacto: IContacto;

  constructor(protected contactoService: ContactoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.contactoService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'contactoListModification',
        content: 'Deleted an contacto'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-contacto-delete-popup',
  template: ''
})
export class ContactoDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ contacto }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ContactoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.contacto = contacto;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/contacto', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/contacto', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
