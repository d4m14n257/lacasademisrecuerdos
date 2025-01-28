"use client"

import { usePathname } from 'next/navigation';
import Link from "next/link";
import logo from "@/../public/static/Logo.png";
import logoSecundary from "@/../public/static/LogoSecondary.png";
import Image from "next/image";
import './header.css';

type Props = {
    lang: string
    nav: {
        home: string,
        about: string,
        room: string,
        tour: string,
        contact: string
    },
}

const color = {
    color: "var(--yellow-color)"
}

export default function Header (props : Props) {
    const { lang, nav } = props;
    const pathname = usePathname()

    return (
        <>
            <header>
                <div className="footer-header-container">
                    {pathname.length > 3 ?
                        <div className='logo'>
                            <Image 
                                src={logoSecundary}
                                alt='Secondary logo'
                                width="100"
                            />
                            <div>
                                <h1>
                                    {pathname.includes("rooms") ? "Rooms" : "Tour"}
                                </h1>
                                <hr />
                            </div>
                        </div>
                        : 
                        <Image 
                            src={logo}
                            alt="Main logo"
                            width="210"
                            priority
                        />
                    }
                    <nav className={pathname.length > 3 ? "nav-site" : ""}>
                        <Link href={`/${lang}`}>{nav.home}</Link>
                        <Link href={`/${lang}#about`}>{nav.about}</Link>
                        <Link href={`/${lang}/rooms`} style={pathname.includes("rooms") ? color : {}}>
                            {nav.room}
                        </Link>
                        <Link href={`/${lang}/tours`} style={pathname.includes("tour") ? color : {}}>
                            {nav.tour}
                        </Link>
                        <Link href={`/${lang}#contact`}>{nav.contact}</Link>
                    </nav>
                </div>
            </header>
            {pathname.length > 3 &&
                <div style={{
                    height: "var(--header-height)"
                }}></div>
            }
        </>
    );
}