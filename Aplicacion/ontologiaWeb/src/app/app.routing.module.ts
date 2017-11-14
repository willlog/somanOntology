import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OntologiaComponent }  from './soman/ontologia.component';


const appRoutes: Routes = [
      {
        path: 'soman',
        component: OntologiaComponent,
      }
  ];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes,
      {
        enableTracing: true, // <-- debugging purposes only
      }
    )
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }