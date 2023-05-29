function defineIcon(pmValue) {
    let value = parseInt(pmValue);

    let pic = "";
    if (value >= 0 && value <= 49) {
        pic = "good.png";
    } else if (value >= 50 && value <= 99) {
        pic = "avg.png";
    } else if (value >= 100 && value <= 129) {
        pic = "bad.png";
    } else if (value >= 130 && value <= 169) {
        pic = "awful.png";
    } else {
        pic = "death.png";
    }

    return pic;
}
