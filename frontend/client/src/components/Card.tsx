import Link from 'next/link';

export default function Card (props : {img: string, title: string, body: string, url: string}) {
    const {img, title, body, url} = props;

    return (
        <article>
            <div>
                <h2>{title}</h2>
                <p>{body}</p>
                {url && 
                    <Link href='/about_us'>
                        Read more
                    </Link>
                }
            </div>
            <img />
        </article>
    );
}