import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IContacto, Contacto } from 'app/shared/model/contacto.model';
import { ContactoService } from './contacto.service';

@Component({
  selector: 'jhi-contacto-update',
  templateUrl: './contacto-update.component.html'
})
export class ContactoUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(50)]],
    celular: []
  });

  constructor(protected contactoService: ContactoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ contacto }) => {
      this.updateForm(contacto);
    });
  }

  updateForm(contacto: IContacto) {
    this.editForm.patchValue({
      id: contacto.id,
      nombre: contacto.nombre,
      celular: contacto.celular
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const contacto = this.createFromForm();
    if (contacto.id !== undefined) {
      this.subscribeToSaveResponse(this.contactoService.update(contacto));
    } else {
      this.subscribeToSaveResponse(this.contactoService.create(contacto));
    }
  }

  private createFromForm(): IContacto {
    return {
      ...new Contacto(),
      id: this.editForm.get(['id']).value,
      nombre: this.editForm.get(['nombre']).value,
      celular: this.editForm.get(['celular']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContacto>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
