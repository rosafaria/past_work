function final = PickCoef(img,num_coef)

    final = zeros(size(img));
    for i= 1:num_coef
        maximo = max(max(abs(img)));
        [l c] = find (abs(img) == maximo, 1);
        final(l,c) = img(l,c);
        img(l,c) = nan;
    end
end

