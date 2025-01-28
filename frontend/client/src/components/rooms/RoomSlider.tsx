"use client"

import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Autoplay } from 'swiper/modules';

import 'swiper/css';
import 'swiper/css/navigation';

type Props = {
    images: {
        name: string;
        main: boolean;
        file: string;
    }[]
}

export default function RoomSlider (props : Props) {
    const { images } = props;

    return (
        <Swiper 
            loop
            navigation
            spaceBetween={30}
            modules={[ Navigation, Autoplay ]}
            autoplay={{
                delay: 2500,
                disableOnInteraction: false,
            }}
            className="swiper-room"
        >
            {images.map((image) => (
                <SwiperSlide
                    key={image.name}
                >
                    <img
                        className="swiper-img"
                        src={`data:image/webp;base64,${image.file}`}
                        alt={image.name}
                    />
                </SwiperSlide>
            ))}
        </Swiper>
    );
}