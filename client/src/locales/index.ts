import { pt } from './pt';
import { en } from './en';
import { useI18n } from 'vue-i18n';
import { Resource } from './resource';

export const resources = {
  pt,
  en
};

export const languages = [
  {
    name: "Português",
    code: "pt",
  },
  {
    name: "English",
    code: "en",
  },
]

export function useTranslate() {
  const { t, locale } = useI18n();

  const translate = (key: Resource): string => {
    return t(key) as string;
  }

  return { translate, locale };
}
