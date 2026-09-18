import { bootstrapApplication } from '@angular/platform-browser';
import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';
import { appConfig } from './app/main/app.config';
import { App } from './app/main/app';

registerLocaleData(localeEs, 'es');

bootstrapApplication(App, appConfig).catch((err) => console.error(err));

