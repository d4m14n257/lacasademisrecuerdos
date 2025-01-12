"use service"

import { redirect } from "next/navigation"

export async function navigation(path: string) {
    redirect(path);
}