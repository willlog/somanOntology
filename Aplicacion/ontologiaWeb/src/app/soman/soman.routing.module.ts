import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { SomanComponent }    from './soman.component';

const somanRoutes: Routes = [
  { path: 'search/:resource', component: SomanComponent }
];

@NgModule({
  imports: [
    RouterModule.forChild(somanRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class SomanRoutingModule { }