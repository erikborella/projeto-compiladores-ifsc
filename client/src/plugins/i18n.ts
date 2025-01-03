import { createI18n } from 'vue-i18n';
import { resources, languages, LANGUAGE_STORAGE_KEY } from '../locales';

function getBrowserLanguage(): string {
  const currentBrowserLanguage: string = window.navigator.language;

  const supportedLanguage: string = languages.find((lang) => currentBrowserLanguage.startsWith(lang.code))?.code || 'pt';

  return supportedLanguage;
}

function getDefaultLanguage(): string {
  const savedLanguage: string | null = localStorage.getItem(LANGUAGE_STORAGE_KEY);

  if (savedLanguage !== null)
    return savedLanguage;

  const browserLanguage: string = getBrowserLanguage();

  localStorage.setItem(LANGUAGE_STORAGE_KEY, browserLanguage);

  return browserLanguage;
}

export default createI18n({
  locale: getDefaultLanguage(),
  fallbackLocale: 'pt',
  messages: resources,
  legacy: false,
  allowComposition: true,
});
