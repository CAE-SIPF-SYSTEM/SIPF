import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../../components/sidebar/sidebar';
import { HeaderComponent } from '../../components/header/header';

@Component({
  selector: 'app-main-layout',
  imports: [CommonModule, SidebarComponent, HeaderComponent],
  template: `
    <div class="flex h-screen overflow-hidden bg-white">
      <app-sidebar #sidebar></app-sidebar>
      
      <div class="flex-1 flex flex-col min-w-0">
        <app-header [title]="pageTitle()"></app-header>
        
        <main class="flex-1 overflow-x-hidden overflow-y-auto bg-white p-6">
          <ng-content></ng-content>
        </main>
      </div>
    </div>
  `
})
export class MainLayoutComponent {
  pageTitle = input<string>('Dashboard');
}
