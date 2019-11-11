import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestSharedModule } from 'app/shared/shared.module';
import { ContactoComponent } from './contacto.component';
import { ContactoDetailComponent } from './contacto-detail.component';
import { ContactoUpdateComponent } from './contacto-update.component';
import { ContactoDeletePopupComponent, ContactoDeleteDialogComponent } from './contacto-delete-dialog.component';
import { contactoRoute, contactoPopupRoute } from './contacto.route';

const ENTITY_STATES = [...contactoRoute, ...contactoPopupRoute];

@NgModule({
  imports: [TestSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ContactoComponent,
    ContactoDetailComponent,
    ContactoUpdateComponent,
    ContactoDeleteDialogComponent,
    ContactoDeletePopupComponent
  ],
  entryComponents: [ContactoDeleteDialogComponent]
})
export class TestContactoModule {}
