

    (function () {
        const carrusel = document.getElementById('heroCarrusel');
        const track = carrusel.querySelector('.carrusel-track');
        const slides = Array.from(carrusel.querySelectorAll('.carrusel-slide'));
        const prevBtn = carrusel.querySelector('.arrow-left');
        const nextBtn = carrusel.querySelector('.arrow-right');
        const dotsContainer = carrusel.querySelector('.carrusel-dots');

        let currentIndex = 0;
        const slideCount = slides.length;
        const intervalMs = 7000; // 7 segundos
        let autoSlideId = null;

        // Crear puntos según número de slides
        slides.forEach((_, index) => {
            const dot = document.createElement('button');
            dot.classList.add('carrusel-dot');
            dot.setAttribute('type', 'button');
            dot.setAttribute('aria-label', 'Ir al slide ' + (index + 1));
            dot.dataset.index = index;
            dotsContainer.appendChild(dot);
        });

        const dots = Array.from(carrusel.querySelectorAll('.carrusel-dot'));

        function updateSlide(position) {
            if (position < 0) {
                position = slideCount - 1;
            } else if (position >= slideCount) {
                position = 0;
            }
            currentIndex = position;

            const offset = -position * 100;
            track.style.transform = 'translateX(' + offset + '%)';

            slides.forEach((slide, i) => {
                slide.classList.toggle('active', i === position);
            });
            dots.forEach((dot, i) => {
                dot.classList.toggle('active', i === position);
            });
        }

        function goToNext() {
            updateSlide(currentIndex + 1);
        }

        function goToPrev() {
            updateSlide(currentIndex - 1);
        }

        function resetAutoSlide() {
            if (autoSlideId) {
                clearInterval(autoSlideId);
            }
            autoSlideId = setInterval(goToNext, intervalMs);
        }

        prevBtn.addEventListener('click', () => {
            goToPrev();
            resetAutoSlide();
        });

        nextBtn.addEventListener('click', () => {
            goToNext();
            resetAutoSlide();
        });

        dots.forEach(dot => {
            dot.addEventListener('click', () => {
                const index = parseInt(dot.dataset.index, 10);
                updateSlide(index);
                resetAutoSlide();
            });
        });

        updateSlide(0);
        resetAutoSlide();
    })();
