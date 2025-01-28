"use client"

import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from 'swiper/modules';

import tripadvisor from '@/../public/static/home/Tripadvisor.png';
import Image from "next/image";

import 'swiper/css';
import 'swiper/css/navigation';

import './slider.css'

type Props = {
    sliders: {
        title: string;
        description: string;
        url: string;
    }[];
}

/* TODO: revisar el css para poner a la izquierda el titulo sin romper el slider */

export default function Slider (props : Props) {
    const { sliders } = props;

    return (
        <Swiper navigation modules={[Navigation]} className="swiper-home">
            {sliders.map((slider) => (
                <SwiperSlide key={slider.title} className="swiper-content">
                    <h1>{slider.title}</h1>
                    <p>{slider.description}</p>
                    <a 
                        href={slider.url}
                        target="_blank"
                    >
                        <Image 
                            src={tripadvisor}
                            alt="Tripadvisor"
                        />
                    </a>
                </SwiperSlide>
            ))}
        </Swiper>
    );
}