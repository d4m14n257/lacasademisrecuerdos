"use server";

const dictionaries = {
    en: (file : string) => import(`./en/${file}.json`).then((module) => module.default),
    es: (file : string) => import(`./es/${file}.json`).then((module) => module.default)
}

export const getDictionary = async(locale : string, file : string) => {
    const dictionaryLoader = dictionaries[locale as "en" | "es"];

    return dictionaryLoader(file);
}