export async function getHotels () {
    try {
        const res = await fetch(`${process.env.NEXT_PUBLIC_SERVER_HOST}/hotel`)

        console.log(res)
    }
    catch (err : unknown) {

    }
}