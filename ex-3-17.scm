;A function to count the number of pairs (cons cells) in some sort of list, tree, etc
;Functions correctly even when cells refer to other cells earlier in the structure

(define (list-pairs x history)
  (if (or (member? x history)
      (not (pair? x)))
      history
      (list-pairs (car x)
        (list-pairs (cdr x) (append (list x) history)))))

(define (member? x history)
  (if (null? history)
    #f
    (if (eq? x (car history))
      #t
      (member? x (cdr history)))))

(define (count-pairs x) (length (list-pairs x '()) 0))

(define (length x n)
  (if (null? x)
    n
    (length (cdr x) (+ n 1))))