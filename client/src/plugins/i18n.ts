import { createI18n } from 'vue-i18n';
import { resources } from '../locales/index';

export default createI18n({
  locale: 'pt',
  fallbackLocale: 'pt',
  messages: resources,
  legacy: false,
  allowComposition: true,
});
