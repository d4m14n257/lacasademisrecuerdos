import Image, { StaticImageData } from "next/image";
import backyard from '@/../public/static/home/Backyard.webp';
import hallway from '@/../public/static/home/Hallway.webp';
import courtyard from '@/../public/static/home/Courtyard.webp';
import about from '@/../public/static/home/About.webp';
import otherLocations from '@/../public/static/home/OtherLocations.webp'
import ContactUs from "@/components/forms/ContactUs";
import Slider from "@/components/home/Slider";
import { getDictionary } from "@/language/getDictionary";
import { getData } from "@/api/getData";
import { Hotel, RoomList } from "@/model/type";
import secondary from '@/../public/static/LogoSecondary.png'

import "./home.css"
import { HOTEL, ROOM_LIST } from "@/constants/endpoints";

type Hotel_temp = {
    id: string;
    name: string;
    summary: string;
    file: StaticImageData;
    url: string;
}

const Hoteles : Hotel_temp[] = [
    {
        id: "dasdas",
        name: "Me encanta Jalatlaco",
        summary: "A litle summary for this.",
        file: secondary,
        url: 'https://www.airbnb.mx/rooms/16614360?viralityEntryPoint=1&s=76&source_impression_id=p3_1737586228_P3-283IQjm2oaPxH'
    },
    {
        id: "daszzzdas",
        name: "La Heredad",
        summary: "A litle summary for this.",
        file: secondary,
        url: 'https://es-l.airbnb.com/rooms/36078219?check_out=2025-05-24&viralityEntryPoint=1&unique_share_id=B992C18A-309C-4E5B-A2BB-1BC5DF1784EF&slcid=56867c3dc27648d38c5735a272d866cc&s=76&feature=share&adults=6&check_in=2025-05-19&channel=native&slug=zIqcXxpT&source_impression_id=p3_1737586229_P3N9K2M-Sv8gRbnn'
    },
    {
        id: "dasxxxxas",
        name: "Estancia valencia",
        summary: "A litle summary for this.",
        file: secondary,
        url: 'https://estanciadevalencia.com/'
    },
    {
        id: "dasyyyyas",
        name: "Estancia valencia",
        summary: "A litle summary for this.",
        file: secondary,
        url: 'https://estanciadevalencia.com/'
    }  
]

type Dict = {
    about: {
        title: string;
        subtitle: string;
        primary_description: string;
        secondary_description: string;
    };
    reviews: {
        title: string;
        description: string;
        url: string;
    }[];
    locations: {
        title: string;
        subtitle: string;
      };
    contact: {
        title: string;
        subtitle: string;
        form: {
            labels: {
                name: string;
                email: string;
                phone: string
                room: string;
                comments: string;
                submit: string;
            }
            errors: {
                id: string;
                name: string;
                email: string;
                invalidEmail: string;
            }
        };
    };
}

export default async function Home ({ params } : { params : { lang: string }}) {
    const dict : Dict = await getDictionary(params.lang, "home")
    const rooms = await getData<RoomList>(`${ROOM_LIST}`, params.lang as "en" | "es");

    return (
        <>
            <Image
                src={courtyard}
                alt="Courtyard"
                className="image"
            />
            <section id="about" className="about-contact-section about-section">
                <h6 className="title">{dict.about.title}</h6>
                <div>
                    <div>
                        <hr/>
                        <h1 className="subtitle">{dict.about.subtitle}</h1>
                        <p className="description">
                            {dict.about.primary_description}<br/><br/>
                            {dict.about.secondary_description}
                        </p>
                    </div>
                    <Image 
                        src={about}
                        alt="About us"
                    />
                </div>
            </section>
            <Image 
                src={hallway}
                alt="Hallway"
                className="image"
            />
            <section id="reviews">
                <Slider 
                    sliders={dict.reviews}
                />
            </section>
            <section id="location">
                <div>
                    <h6 className="title">{dict.locations.title}</h6>
                    <h1 className="subtitle">{dict.locations.subtitle}</h1>
                    <div className="list-location">
                        {Hoteles.map((hotel) => (
                            <div key={hotel.id}>
                                <Image 
                                    src={hotel.file}
                                    alt={hotel.name}
                                    height={150}
                                />
                                <div className="list-location-text">
                                    <h3>{hotel.name}</h3>
                                    <p>{hotel.summary}</p>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
                <Image 
                    src={otherLocations}
                    alt="Me Encanta Jalatlaco"
                />
            </section>
            <section id="contact" className="about-contact-section">
                <div>
                    <div>
                        elemento
                    </div>
                    <div>
                        <h3 className="title">{dict.contact.title}</h3>
                        <h1 className="subtitle">{dict.contact.subtitle}</h1>
                        <ContactUs 
                            labels={dict.contact.form.labels}
                            errors={dict.contact.form.errors}
                            rooms={rooms.data as RoomList[]}
                            language={params.lang as "en" | "es"}
                        />
                    </div>
                </div>
            </section>
            <Image 
                src={backyard}
                alt="Backyard"
                className="image"
            />
            
        </>
    );
}